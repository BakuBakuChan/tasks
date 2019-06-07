node{
   
   properties(
      [parameters(
      [string(defaultValue: '1', description: '', name: 'USERS', trim: false),
       string(defaultValue: '60', description: '', name: 'RUMPUP', trim: false),
       string(defaultValue: '60', description: '', name: 'DURATION', trim: false)])])
   
   stage("git pull"){
      git branch: 'JMeter_task', url: 'https://github.com/BakuBakuChan/tasks'
   }
   stage("Run gatling project"){
     bat label: '', script: '''dir
    cd apache-jmeter-5.1.1\\bin
    jmeter.bat -Jjmeter.save.saveservice.output_format=xml -n -t ..\\..\\newOne.jmx -l Reports\\test.jtl -Jusers=%USERS% -JrumpUp=%RUMPUP% -Jduration=%DURATION%'''
    }
   stage("Report"){
      perfReport filterRegex: '', sourceDataFiles: 'Reports\\test.jtl'
   }
}
