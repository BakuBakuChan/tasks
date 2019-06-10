node{
   
   properties(
      [parameters(
      [string(defaultValue: '1', description: '', name: 'USERS', trim: false),
       string(defaultValue: '60', description: '', name: 'RAMP_UP', trim: false),
       string(defaultValue: '60', description: '', name: 'DURATION', trim: false)])])
   
   stage("git pull"){
      git branch: 'JMeter_task', url: 'https://github.com/BakuBakuChan/tasks'
   }
   stage("Run gatling project"){
     bat label: '', script: '''dir
    cd apache-jmeter-5.1.1\\bin
    mkdir Reports\\reports
    jmeter.bat -n -t ..\\..\\newOne.jmx -l Reports\\test.cvl -e -o Reports\\reports -Jusers=%USERS% -JrumpUp=%RAMP_UP% -Jduration=%DURATION%'''
    }
   stage("Report"){
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true,
                 reportDir: '\\apache-jmeter-5.1.1\\bin\\Reports\\reports', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
   }
}
