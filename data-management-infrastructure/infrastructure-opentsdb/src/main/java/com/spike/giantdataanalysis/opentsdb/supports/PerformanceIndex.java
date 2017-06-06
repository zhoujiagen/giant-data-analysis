package com.spike.giantdataanalysis.opentsdb.supports;


public class PerformanceIndex {



    /**
     * <pre>
     * CPU
     * Disk
     * DiskIO
     * Network
     * Kernel
     * Sys
     * Process
     * REF: https://book.open-falcon.org/zh/faq/linux-metrics.html
     * </pre>
     */
    public static class OperationSystem {

        public static enum CPU {
            cpu_idle(0, Metrics.MetricType.PERCENT, "cpu.idle", "Percentage of time that the CPU or CPUs were idle and the system did not have an outstanding disk I/O request"), //
            cpu_busy(1, Metrics.MetricType.PERCENT, "cpu.busy", "与cpu.idle相对，他的值等于100减去cpu.idle"), //
            cpu_guest(2, Metrics.MetricType.PERCENT, "cpu.guest", "Percentage of time spent by the CPU or CPUs to run a virtual processor"), //
            cpu_iowait(3, Metrics.MetricType.PERCENT, "cpu.iowait", "Percentage of time that the CPU or CPUs were idle during which the system had an outstanding disk I/O request"), //
            cpu_irq(4, Metrics.MetricType.PERCENT, "cpu.irq", "Percentage of time spent by the CPU or CPUs to service hardware interrupts"), //
            cpu_softirq(5, Metrics.MetricType.PERCENT, "cpu.softirq", "Percentage of time spent by the CPU or CPUs to service software interrupts"), //
            cpu_nice(6, Metrics.MetricType.PERCENT, "cpu.nice", "Percentage of CPU utilization that occurred while executing at the user level with nice priority"), //
            cpu_steal(7, Metrics.MetricType.PERCENT, "cpu.steal",
                    "Percentage of time spent in involuntary wait by the virtual CPU or CPUs while the hypervisor was servicing another virtual processor"), //
            cpu_system(8, Metrics.MetricType.PERCENT, "cpu.system", "Percentage of CPU utilization that occurred while executing at the system level (kernel)"), //
            cpu_user(9, Metrics.MetricType.PERCENT, "cpu.user", "Percentage of CPU utilization that occurred while executing at the user level (application)"), //
            cpu_cnt(10, Metrics.MetricType.INT, "cpu.cnt", "cpu核数"), //
            cpu_switches(11, Metrics.MetricType.INT, "cpu.switches", "cpu上下文切换次数，计数器类型");

            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            CPU(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }

            public static CPU E(int index) {
                for (CPU e : CPU.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public Metrics.MetricType getType() {
                return type;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

        }

        public static enum Disk {
            df_bytes_free(0, Metrics.MetricType.INT, "df.bytes.free", "磁盘可用量, int64"), //
            df_bytes_free_percent(1, Metrics.MetricType.PERCENT, "df.bytes.free.percent", "磁盘可用量占总量的百分比, float64, 比如32.1"), //
            df_bytes_total(2, Metrics.MetricType.INT, "df.bytes.total", "磁盘总大小, int64"), //
            df_bytes_used(3, Metrics.MetricType.INT, "df.bytes.used", "磁盘已用大小, int64"), //
            df_bytes_used_percent(4, Metrics.MetricType.PERCENT, "df.bytes.used.percent", "磁盘已用大小占总量的百分比, float64"), //
            df_inodes_total(5, Metrics.MetricType.INT, "df.inodes.total", "inode总数, int64"), //
            df_inodes_free(6, Metrics.MetricType.INT, "df.inodes.free", "可用inode数目, int64"), //
            df_inodes_free_percent(7, Metrics.MetricType.PERCENT, "df.inodes.free.percent", "可用inode占比, float64"), //
            df_inodes_used(8, Metrics.MetricType.INT, "df.inodes.used", "已用的inode数据, int64"), //
            df_inodes_used_percent(9, Metrics.MetricType.PERCENT, "df.inodes.used.percent", "已用inode占比, float64");

            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            Disk(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }

            public static Disk E(int index) {
                for (Disk e : Disk.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }

        }

        public static enum DiskIO {
            disk_io_ios_in_progress(0, Metrics.MetricType.INT, "disk.io.ios_in_progress", "Number of actual I/O requests currently in flight."), //
            disk_io_msec_read(1, Metrics.MetricType.INT, "disk.io.msec_read", "Total number of ms spent by all reads."), //
            disk_io_msec_total(2, Metrics.MetricType.INT, "disk.io.msec_total", "Amount of time during which ios_in_progress >= 1."), //
            disk_io_msec_weighted_total(3, Metrics.MetricType.INT, "disk.io.msec_weighted_total", "Measure of recent I/O completion time and backlog."), //
            disk_io_msec_write(4, Metrics.MetricType.INT, "disk.io.msec_write", "Total number of ms spent by all writes."), //
            disk_io_read_merged(5, Metrics.MetricType.INT, "disk.io.read_merged", "Adjacent read requests merged in a single req."), //
            disk_io_read_requests(6, Metrics.MetricType.INT, "disk.io.read_requests", "Total number of reads completed successfully."), //
            disk_io_read_sectors(7, Metrics.MetricType.INT, "disk.io.read_sectors", "Total number of sectors read successfully."), //
            disk_io_write_merged(8, Metrics.MetricType.INT, "disk.io.write_merged", "Adjacent write requests merged in a single req."), //
            disk_io_write_requests(9, Metrics.MetricType.INT, "disk.io.write_requests", "total number of writes completed successfully."), //
            disk_io_write_sectors(10, Metrics.MetricType.INT, "disk.io.write_sectors", "total number of sectors written successfully."), //
            disk_io_read_bytes(11, Metrics.MetricType.INT, "disk.io.read_bytes", "单位是byte的数字"), //
            disk_io_write_bytes(12, Metrics.MetricType.INT, "disk.io.write_bytes", "单位是byte的数字"), //
            disk_io_avgrq_sz(13, Metrics.MetricType.INT, "disk.io.avgrq_sz", "下面几个值就是iostat -x 1看到的值"), //
            disk_io_avgqu_sz(14, Metrics.MetricType.INT, "disk.io.avgqu-sz", ""), //
            disk_io_await(15, Metrics.MetricType.INT, "disk.io.await", ""), //
            disk_io_svctm(16, Metrics.MetricType.INT, "disk.io.svctm", ""), //
            disk_io_util(17, Metrics.MetricType.PERCENT, "disk.io.util", "是个百分数，比如56.43，表示56.43%");

            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            DiskIO(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }

            public static DiskIO E(int index) {
                for (DiskIO e : DiskIO.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }
        }

        public static enum Network {

            net_if_in_bytes(0, Metrics.MetricType.INT, "net.if.in.bytes", ""), //
            net_if_in_compressed(1, Metrics.MetricType.INT, "net.if.in.compressed", ""), //
            net_if_in_dropped(2, Metrics.MetricType.INT, "net.if.in.dropped", ""), //
            net_if_in_errors(3, Metrics.MetricType.INT, "net.if.in.errors", ""), //
            net_if_in_fifo_errs(4, Metrics.MetricType.INT, "net.if.in.fifo.errs", ""), //
            net_if_in_frame_errs(5, Metrics.MetricType.INT, "net.if.in.frame.errs", ""), //
            net_if_in_multicast(6, Metrics.MetricType.INT, "net.if.in.multicast", ""), //
            net_if_in_packets(7, Metrics.MetricType.INT, "net.if.in.packets", ""), //
            net_if_out_bytes(8, Metrics.MetricType.INT, "net.if.out.bytes", ""), //
            net_if_out_carrier_errs(9, Metrics.MetricType.INT, "net.if.out.carrier.errs", ""), //
            net_if_out_collisions(10, Metrics.MetricType.INT, "net.if.out.collisions", ""), //
            net_if_out_compressed(11, Metrics.MetricType.INT, "net.if.out.compressed", ""), //
            net_if_out_dropped(12, Metrics.MetricType.INT, "net.if.out.dropped", ""), //
            net_if_out_errors(13, Metrics.MetricType.INT, "net.if.out.errors", ""), //
            net_if_out_fifo_errs(14, Metrics.MetricType.INT, "net.if.out.fifo.errs", ""), //
            net_if_out_packets(15, Metrics.MetricType.INT, "net.if.out.packets", ""), //
            net_if_total_bytes(16, Metrics.MetricType.INT, "net.if.total.bytes", ""), //
            net_if_total_dropped(17, Metrics.MetricType.INT, "net.if.total.dropped", ""), //
            net_if_total_errors(18, Metrics.MetricType.INT, "net.if.total.errors", ""), //
            net_if_total_packets(19, Metrics.MetricType.INT, "net.if.total.packets", ""), //

            net_port_listen(20, Metrics.MetricType.INT, "net.port.listen", "");//


            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            Network(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }

            public static Network E(int index) {
                for (Network e : Network.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }
        }

        public static enum Kernel {

            kernel_maxfiles(0, Metrics.MetricType.INT, "kernel_maxfiles", " 读取的/proc/sys/fs/file-max"), //
            kernel_files_allocated(1, Metrics.MetricType.INT, "kernel_files.allocated", "读取的/proc/sys/fs/file-nr第一个Field"), //
            kernel_files_left(2, Metrics.MetricType.INT, "kernel_files.left", "值=kernel_maxfiles-kernel_files.allocated"), //
            kernel_maxproc(3, Metrics.MetricType.INT, "kernel_maxproc", "读取的/proc/sys/kernel/pid_max");

            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            Kernel(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }

            public static Kernel E(int index) {
                for (Kernel e : Kernel.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }
        }

        public static enum Sys {
            sys_ntp_offset(0, Metrics.MetricType.INT, "sys.ntp.offset", "");

            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            Sys(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }


            public static Sys E(int index) {
                for (Sys e : Sys.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }
        }

        public static enum Process {
            process_cpu_all(0, Metrics.MetricType.PERCENT, "process_cpu.all", "进程和它的子进程使用的sys+user的cpu，单位是jiffies"), //
            process_cpu_sys(1, Metrics.MetricType.PERCENT, "process_cpu.sys", "进程和它的子进程使用的sys cpu，单位是jiffies"), //
            process_cpu_user(2, Metrics.MetricType.PERCENT, "process_cpu.user", "进程和它的子进程使用的user cpu，单位是jiffies"), //
            process_swap(3, Metrics.MetricType.INT, "process_swap", "进程和它的子进程使用的swap，单位是page"), //
            process_fd(4, Metrics.MetricType.INT, "process_fd", "进程使用的文件描述符个数"), //
            process_mem(5, Metrics.MetricType.INT, "process_mem", "进程占用内存，单位byte");


            private int index;
            private Metrics.MetricType type;
            private String metric;
            private String comment;

            Process(int index, Metrics.MetricType type, String metric, String comment) {
                this.index = index;
                this.type = type;
                this.metric = metric;
                this.comment = comment;
            }


            public static Process E(int index) {
                for (Process e : Process.values()) {
                    if (index == e.getIndex()) return e;
                }
                return null;
            }

            public int getIndex() {
                return index;
            }

            public String getMetric() {
                return metric;
            }

            public String getComment() {
                return comment;
            }

            public Metrics.MetricType getType() {
                return type;
            }
        }

    }
}
