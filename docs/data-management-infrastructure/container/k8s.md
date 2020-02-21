# Kubernetes

- [Kubernetes Documentation](https://kubernetes.io/docs/home/)
- [Reference](https://kubernetes.io/docs/reference/): API, CLI, Config, Kubeadm, and Design Docs
- [kubernetes/kubernetes](https://github.com/kubernetes/kubernetes)
- [fabric8io/kubernetes-client](https://github.com/fabric8io/kubernetes-client): Java client for Kubernetes & OpenShift

- [Minikube](https://kubernetes.io/docs/setup/learning-environment/minikube/): Minikube runs a single-node Kubernetes cluster inside a Virtual Machine (VM) on your laptop for users looking to try out Kubernetes or develop with it day-to-day.
- [Kubeadm](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm/): Kubeadm is a tool built to provide kubeadm init and kubeadm join as best-practice “fast paths” for creating Kubernetes clusters.

## 概念

- [Concepts](https://kubernetes.io/docs/concepts/)

### Node

- 物理机, 虚拟机
- 通常在一个节点上运行几百个Pod
- 集群管理: Master节点, 工作节点; Master节点上运行kube-apiserver、kube-controller-manager和kube-scheduler; 最小的运行单元是Pod; 节点上运行Kubelet、kube-proxy和docker daemon服务进程.

### Pod

- 将每个服务进程包装到Pod中, 使其成为Pod中运行的一个容器
- 每个Pod中运行一个Pause容器, 其它为业务容器
- 在每个Pod的容器中都增加了一组Service相关的环境变量, 记录从服务名到虚拟IP地址的映射关系

### Label

- 键值对
- 附加的对象: Pod、Service、RC、Node等
- Service与Pod的关联: 在Pod上定义标签, 在Service上定义标签选择器:

### RC: Replication Controller

> A [Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) that configures a [ReplicaSet](https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/) is now the recommended way to set up replication.

- RC定义文件: 目标Pod的定义、目标Pod需要运行的副本数量、要监控的目标Pod的标签

### Service

- 拥有一个唯一指定的名字
- 拥有一个虚拟IP(ClusterIP, Service IP或VIP)和端口号; 对外提供服务的type: NodePort和LoadBalancer
- 被映射到提供远程服务能力的一组容器应用上
- 在所定义的Pod正常启动后, 根据Service的定义创建与Pod对应的端点(Endpoint)对象, 以建立Service与Pod的对应关系

### Volume

- Pod中能够被多个容器访问的共享目录

### Namespace

- 逻辑分组: 将系统内部的对象分配到不同的命名空间中

### Annotation

- 键值对
- 用户任意定义的附加信息, 便于外部工具查找


## 组件

- [Kubernetes Architecture](http://git.k8s.io/community/contributors/design-proposals/architecture/architecture.md)
- [Kubernetes Design Overview](http://git.k8s.io/community/contributors/design-proposals)

![K8S组件图](./images/components-of-kubernetes.png)

### Control Plane组件

- kube-apiserver: Kubernets API server的实现
- etcd: 集群数据的后端存储
- kube-scheduler: 监听未指派节点的Pod创建事件, 选择节点运行Pod
- kube-controller-manager: 运行控制器进程: 节点控制器、RC、端点控制器和服务账号与Token控制器
- cloud-controller-manager: 运行与底层云服务提供商交互的控制器; 涉及节点控制器、路由控制器、服务控制器、卷控制器

controller-manager内部包含

- Node Controller
- Namespace Controller
- Service Controller
- Endpoint Controller
- RC: Replicaiton Controller
- ResourceQuota Controller
- ServiceAccount Controller
- Token Controller

### Node组件

- kubelet: 集群中节点上运行的agent, 确保PodSpecs中描述的容器健康运行
- kube-proxy: 集群中节点上运行的网络代理, 实现部分Service的概念; 维护节点上的网络规则
- Container Runtime: 负责运行容器的软件, 有Docker、containerd、CRI-O和任意Kubernets CRI的实现.
