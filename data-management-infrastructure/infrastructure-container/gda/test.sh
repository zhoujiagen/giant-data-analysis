# hadoop
# docker-compose -f docker-compose_hadoop.yml up -d
# docker-compose -f docker-compose_hadoop.yml kill namenode datanode1 historyserver
# docker-compose -f docker-compose_hadoop.yml rm -f namenode datanode1 historyserver

# hive
# docker-compose -f docker-compose_hive.yml up -d
# docker-compose -f docker-compose_hive.yml kill namenode datanode1 historyserver hive-metastore-postgresql hivemetastore hiveserver
# docker-compose -f docker-compose_hive.yml rm -f namenode datanode1 historyserver hive-metastore-postgresql hivemetastore hiveserver
# docker exec -it hiveserver bash
