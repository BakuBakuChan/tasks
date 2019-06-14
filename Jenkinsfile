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
     bat label: '', script: '''dir
    cd apache-jmeter-5.1.1\\bin
    mkdir Reports\\resports
    jmeter.bat -n -t ..\\..\\newOne.jmx -l Reports\\test.cvl -e -o Reports\\resports -Jusers=%USERS% -JrumpUp=%RAMP_UP% -Jduration=%DURATION%'''
    }
   stage("Report"){
    publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 
                 '\\apache-jmeter-5.1.1\\bin\\Reports\\reports', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
   }
}
