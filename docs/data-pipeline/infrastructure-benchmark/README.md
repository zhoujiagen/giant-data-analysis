
# 0 实践版本

+ [Metrics](https://metrics.dropwizard.io) 3.2.3
+ [OpenJDK jmh](http://openjdk.java.net/projects/code-tools/jmh/) 1.21
+ [OpenJDK jol](http://openjdk.java.net/projects/code-tools/jol/)

# 1 资源

+ [OpenJDK Code Tools Project](http://openjdk.java.net/projects/code-tools/)
+ [Caliper](https://github.com/google/caliper) NO!

# 2 运行实例

# 3 安装和配置


# OpenJDK jmh

+ HotSpot PrintAssembly: https://wiki.openjdk.java.net/display/HotSpot/PrintAssembly

      zhoujiagen@gda:~/giant-data-analysis/data-pipeline/infrastructure-benchmark$ cd $JAVA_HOME
      zhoujiagen@gda:/usr/lib/jvm/java-8-openjdk-amd64$ sudo cp ~/hsdis-amd64.so jre/lib/amd64/
      zhoujiagen@gda:/usr/lib/jvm/java-8-openjdk-amd64$ ls jre/lib/amd64/
      hsdis-amd64.so   libawt.so           libhprof.so          libj2pkcs11.so       libjavalcms.so  libjsound.so      libnio.so           libsunec.so
      jli              libawt_headless.so  libicedtea-sound.so  libjaas_unix.so      libjawt.so      libjsoundalsa.so  libnpt.so           libunpack.so
      jvm.cfg          libawt_xawt.so      libinstrument.so     libjava.so           libjdwp.so      libmanagement.so  libsaproc.so        libverify.so
      jvm.cfg-default  libdt_socket.so     libj2gss.so          libjava_crw_demo.so  libjsdt.so      libmlib_image.so  libsctp.so          libzip.so
      libattach.so     libfontmanager.so   libj2pcsc.so         libjavajpeg.so       libjsig.so      libnet.so         libsplashscreen.so  server
      zhoujiagen@gda:/usr/lib/jvm/java-8-openjdk-amd64$


+ JVM Anatomy Park #1: Lock Coarsening and Loops: https://shipilev.net/jvm-anatomy-park/1-lock-coarsening-for-loops/
+ perf Examples: http://www.brendangregg.com/perf.html
+ How to install “perf” monitoring tool?: https://askubuntu.com/questions/50145/how-to-install-perf-monitoring-tool/306683
+ How To Update Linux Kernel In Ubuntu: https://phoenixnap.com/kb/how-to-update-kernel-ubuntu
+ Building hsdis: https://github.com/AdoptOpenJDK/jitwatch/wiki/Building-hsdis

      zhoujiagen@gda:~/giant-data-analysis/data-pipeline/infrastructure-benchmark$ java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly \
        -jar target/jmh-micro-benchmarks.jar com.spike.giantdataanalysis.benchmark.tools.jmh.LockRoach \
        -prof perfasm:mergeMargin=1000
