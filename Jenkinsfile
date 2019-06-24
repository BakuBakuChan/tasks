node{
   
   properties(
      [parameters(
      [string(defaultValue: '1', description: '', name: 'USERS', trim: false),
       string(defaultValue: '1', description: 'in seconds', name: 'RAMP_UP', trim: false),
       string(defaultValue: '60', description: 'in seconds', name: 'DURATION', trim: false)])])
   
   stage("Git pull"){
      git branch: 'JMeter_task', url: 'https://github.com/BakuBakuChan/tasks'
   }
   stage("Run JMeter project"){
      sh label: '', script: '''
      export timestamp=$(date +%Y%m%d_%H%M%S) && \\
      export volume_path=/var/lib/jenkins/workspace/JMeter_Gatling_docker_ubuntu/newItem && \\
      export jmeter_path=/mnt/jmeter
      docker run \\
      --rm \\
      --name nameOne \\
      --volume ${volume_path}:${jmeter_path} \\
      vmarrazzo/jmeter \\
      -n "-Jusers=%USERS% -JrumpUp=%RAMP_UP% -Jduration=%DURATION%" \\
      -t ${jmeter_path}/newOne.jmx \\
      -l ${jmeter_path}/tmp/result_${timestamp}.jtl \\
	  -e -o Reports/resports \\
      -j ${jmeter_path}/tmp/jmeter_${timestamp}.log'''  
    }
    
     stage("Report"){
     sh label: '', script: ''' 
     cd /tmp
     mkdir report
     cd /var/lib/jenkins/workspace/JMeter_Gatling_docker_ubuntu/newItem/Repo*
     cp index.html /tmp/report
     cp -r sbadmin2-1.0.7 /tmp/report
     cp -r content /tmp/report'''
    publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 
                 '/tmp/report', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
   }
}
