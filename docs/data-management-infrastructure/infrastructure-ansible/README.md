
# 0 实践版本

	ansible-2.3.0.0

# 1 资源

+ [ansiblebook - Github](https://github.com/ansiblebook/ansiblebook)


# 2 运行实例

# 3 安装和配置

## Vagrant

使用VirtualBox 5.1.10 r112026 (Qt5.6.2)


	$ vagrant -v
	Vagrant 1.9.3
	$ vagrant init ubuntu/trusty64
	$ vagrant up
	$ vagrant ssh
	Welcome to Ubuntu 14.04.5 LTS (GNU/Linux 3.13.0-116-generic x86_64)

	# 查看Vagrant SSH配置
	$ vagrant ssh-config
	Host default
	  HostName 127.0.0.1
	  User vagrant
	  Port 2222
	  UserKnownHostsFile /dev/null
	  StrictHostKeyChecking no
	  PasswordAuthentication no
	  IdentityFile /Users/jiedong/github_local/giant-data-analysis/data-management-infrastructure/infrastructure-ansible/playbooks/.vagrant/machines/default/virtualbox/private_key
	  IdentitiesOnly yes
	  LogLevel FATAL

  # 在Vagrantfile中添加端口映射后让新配置生效
  $ vagrant reload

## Ansible

	$ sudo pip install ansible
	$ ansible --version
	ansible 2.3.0.0
	$ which ansible
	/usr/local/bin/ansible
	# 测试连通性
	$ ansible testserver -m ping
	testserver | SUCCESS => {
	    "changed": false,
	    "ping": "pong"
	}
	# sudo运行: -s
	$ ansible testserver -s -a "tail /var/log/syslog"

	# 执行playbook
	$ ansible-playbook web-notls.yml

	# 查看模块的帮助文档
	$ ansible-doc command

## TLS

使用OpenSSL生成自签名证书

	$ openssl req -x509 -nodes -days 3650 \
		-newkey rsa:2048 -subj /CN=localhost \
		-keyout files/nginx.key -out files/nginx.crt
