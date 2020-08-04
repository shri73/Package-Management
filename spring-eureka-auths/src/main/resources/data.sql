REPLACE INTO `stores` (store_no) VALUES ('S111');
REPLACE INTO `stores` (store_no) VALUES ('S112');
REPLACE INTO `stores` (store_no) VALUES ('S113');
REPLACE INTO `stores` (store_no) VALUES ('S114');

REPLACE INTO `beacons` (uuid, major, minor, store_no) VALUES ('1', '1', '1', 'S111');
REPLACE INTO `beacons` (uuid, major, minor, store_no) VALUES ('1', '2', '2', 'S112');
REPLACE INTO `beacons` (uuid, major, minor, store_no) VALUES ('1', '2', '3', 'S113');

