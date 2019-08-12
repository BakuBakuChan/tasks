#!groovy

//      [Define variables]
PARAMETERS_FILE="Tests/Common/parameters.txt"
test_env_url = null
execution_curl = null
vision_curl = null
masterIp = null
ips = null 
jobUsers = users
jmeterStackName = 'quality-engineering-jmeter-' + env.BUILD_NUMBER
jobName = "lims-proxy-api-perftests"
instancePrivateIp = '10.120.50.1'
slave_loop = 1
countOfInstances = 0

pipeline {
 steps{script{
 name = env.JOB_BASE_NAME
 echo "${name}"
 }}
}
