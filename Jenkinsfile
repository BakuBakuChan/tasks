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
      export volume_path=/tmp/tasks && \\
      export jmeter_path=/mnt/jmeter
      docker run \\
      --rm \\
      --name jmetertest \\
      --link=influxdb \\
      --volume ${volume_path}:${jmeter_path} \\
        jmeter \\
      -n "-Jusers=%USERS% -JrumpUp=%RAMP_UP% -Jduration=%DURATION%" \\
      -t ${jmeter_path}/TestSL.jmx\\
      -l ${jmeter_path}/tmp/result_${timestamp}.jtl \\
      -j ${jmeter_path}/tmp/jmeter_${timestamp}.log'''  
    }
}
