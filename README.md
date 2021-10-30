# Doctor Scheduler
Schedule doctors' service days to fit their requirements as much as possible.

# Deployment Notes

## Port Forwarding

This helps running the application on standard http ports instead of Tomcat ports

Tested on:
 * CentOS 7
 * Red Hat 8.4

<pre>
# root/sudo access needed or it won't work
# install iptables-services if not installed yet
# rpm -aq iptables-services
# yum install iptables-services

iptables -I INPUT 1 -p tcp --dport 8443 -j ACCEPT
iptables -I INPUT 1 -p tcp --dport 8080 -j ACCEPT
iptables -I INPUT 1 -p tcp --dport 443 -j ACCEPT
iptables -I INPUT 1 -p tcp --dport 80 -j ACCEPT
iptables -L -n

iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080
iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 443 -j REDIRECT --to-port 8443
iptables -L -n -t nat

service iptables save
chkconfig iptables on
</pre>

