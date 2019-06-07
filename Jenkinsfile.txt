node{
   stage("Run gatling project"){
     bat label: '', script: '''dir
    cd apache-jmeter-5.1.1\\bin
    jmeter.bat -Jjmeter.save.saveservice.output_format=xml -n -t ..\\..\\newOne.jmx -l Reports\\test.jtl -Jusers=%USERS% -JrumpUp=%RUMPUP% -Jduration=%DURATION%'''
    }
}