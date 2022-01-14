# Doctor Scheduler
Schedule doctors' shift days to fit their requirements as much as possible.

## Deployment Tips & Tricks
Tested on:
* CentOS 7
* Red Hat 8.4

### Start Application
example command:

    java -jar doctor-scheduler.jar

example script:

    #!/bin/bash
    /home/ec2-user/java/graalvm-ee-java17-21.3.0/bin/java -jar /home/ec2-user/doc-scheduler/doctor-scheduler-1.0.3.jar > /dev/null

### Automatic Startup

    vi /etc/systemd/system/doc-scheduler.service

and then

    [Unit]
    Description=DoctorScheduler Spring Boot application
    After=network.target
    
    [Service]
    Type=simple
    User=ec2-user
    ExecStart=/bin/bash /home/ec2-user/doc-scheduler/start.sh
    
    [Install]
    WantedBy=default.target

and then

    systemctl daemon-reload
    systemctl enable doc-scheduler.service
    systemctl start doc-scheduler.service

### Port Forwarding

This helps running the application on standard http ports instead of Tomcat ports

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
