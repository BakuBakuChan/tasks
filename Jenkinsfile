node{
   stage("git pull"){
      git branch: '*/JMeter_task', url: 'https://github.com/BakuBakuChan/tasks'
   }
   stage("Run gatling project"){
     bat label: '', script: '''dir
    cd apache-jmeter-5.1.1\\bin
    jmeter.bat -Jjmeter.save.saveservice.output_format=xml -n -t ..\\..\\newOne.jmx -l Reports\\test.jtl -Jusers=%USERS% -JrumpUp=%RUMPUP% -Jduration=%DURATION%'''
    }
}
