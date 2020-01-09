apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${project.name}
  namespace: ns-${profile}
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      run: ${project.name}
  template:
    metadata:
      labels:
        run: ${project.name}
    spec:
      affinity:
        nodeAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 1
            preference:
              matchExpressions:
              - key: node-label
                operator: In
                values:
                - ${profile}
      containers:
      <#if needClient=="true">
      - name: ${project.name}
        image: nexusserver:${nexusPort}/${project.build.finalName}-client:${profile}-${project.version}
        imagePullPolicy: IfNotPresent
        lifecycle:
          postStart:
            exec:
              command: ["/bin/sh", "-c", "/home/DockerCommand/wechat.sh '${project.name}-${profile} is start!' '${msgGroup}'"]
          preStop:
            exec:
              command: ["/bin/sh", "-c", "/home/DockerCommand/wechat.sh '${project.name}-${profile} is stop!' '${msgGroup}'"]
        ports:
        - containerPort: 8080
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "${profile}"
        - name: DEVLOPER_NAME
          value: "${DEVLOPER_NAME}-${profile}"
        <#list env?keys as key>
        - name: ${key}
          value: "${env[key]}"
        </#list>
        resources:
          requests:
            memory: "1000M"
            cpu: "50m"
          limits:
            memory: "2048M"
            cpu: "1.5"
        #args:
          # - --auto-generate-certificates
          # Uncomment the following line to manually specify Kubernetes API server Host
          # If not specified, Dashboard will attempt to auto discover the API server and connect
          # to it. Uncomment only if the default does not work.
          # - --apiserver-host=http://my-address:port
        volumeMounts:
        - mountPath: /home/DockerCommand/
          name: boot-volume
        #livenessProbe:
          #httpGet:
            #scheme: HTTPS
            #path: /
            #port: 8443
          #initialDelaySeconds: 30
          #timeoutSeconds: 30
      ###################################################################################################
      </#if>
      - name: ${project.name}-server
        image: nexusserver:${nexusPort}/${project.build.finalName}-server:${profile}-${project.version}
        imagePullPolicy: IfNotPresent
        lifecycle:
          postStart:
            exec:
              command: ["/bin/sh", "-c", "/home/DockerCommand/wechat.sh '${project.name}-server-${profile} is start!' '${msgGroup}'"]
          preStop:
            exec:
              command: ["/bin/sh", "-c", "/home/DockerCommand/wechat.sh '${project.name}-server-${profile} is stop!' '${msgGroup}'"]
        ports:
        - containerPort: 8080
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "${profile}"
        - name: DEVLOPER_NAME
          value: "${DEVLOPER_NAME}-${profile}"
        <#list env?keys as key>
        - name: ${key}
          value: "${env[key]}"
        </#list>
        resources:
          requests:
            memory: "1000M"
            cpu: "50m"
          limits:
            memory: "2048M"
            cpu: "1.5"
        #args:
          # - --auto-generate-certificates
          # Uncomment the following line to manually specify Kubernetes API server Host
          # If not specified, Dashboard will attempt to auto discover the API server and connect
          # to it. Uncomment only if the default does not work.
          # - --apiserver-host=http://my-address:port
        volumeMounts:
        - mountPath: /home/DockerCommand/
          name: boot-volume
        #livenessProbe:
          #httpGet:
            #scheme: HTTPS
            #path: /
            #port: 8443
          #initialDelaySeconds: 30
          #timeoutSeconds: 30
      ###################################################################################################
      volumes:
      - name: boot-volume
        hostPath:
          path: /home/DockerCommand/
          type: Directory
      #serviceAccountName: eureka
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
      - key: node-role.kubernetes.io/master
        effect: NoSchedule
      imagePullSecrets:
        - name: ${regsecret}
---