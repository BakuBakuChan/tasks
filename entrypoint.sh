export JVM_ARGS="-Xmn${n}m -Xms${s}m -Xmx${x}m"

echo "START Running Jmeter on `date`"

echo "JVM_ARGS=${JVM_ARGS}"

echo "jmeter args=$@"

jmeter $@

echo "END Running Jmeter on `date`"
