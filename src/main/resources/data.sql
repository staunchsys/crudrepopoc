-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Table structure for table `client`
--
USE poc_db;
TRUNCATE TABLE `client`;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'234, 1st Floor, Above Hotel Vijay Vihar, Chickpet, Banglore','Vijay pvt. ltd. ','sales@vijay.com','Mr. Vijay Brahmbhatt','+91 9999999991'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'75 Mun Mkt., Sainath Road, Malad (west), Mumbai','Sainath pvt. ltd. ','sales@sainath.com','Mr. Sainath Shrivastava','+91 9999999992'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'B/5 Kasturchand Compound, Dadar West, Mumbai','Kasturchand pvt. ltd. ','sales@kasturchand.com','Mr. Kasturchand Bhatt','+91 9999999993'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'604, Prabhat Kiran, Delhi','Manan pvt. ltd. ','sales@manan.com','Mr. Rushikesh Brahmbhatt','+91 9999999994'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'K-103, Ansa Industrial Estate, Saki Vihar Rd, Andheri(e), Mumbai','Kiran pvt. ltd. ','sales@Kiran.com','Mr. Kiran Brahmbhatt','+91 9999999995'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'2, Padam, Gamadia Road, Peddar Road, Mumbai','Manan pvt. ltd. ','sales@gamadia.com','Mr. Padam Gamadia','+91 9999999996'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'380, 380,averdblr-2, Avenue Road, Banglore','Manan pvt. ltd. ','sales@manan.com','Mr. Rushikesh Brahmbhatt','+91 9999999997'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'C 11, J B Tito Marg, Near Chirag Flyover, Panchsheel Enclave, Delhi','Manan pvt. ltd. ','sales@Chirag.com','Mr. Chirag Brahmbhatt','+91 9999999998'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Plot#68 Flat#4b, Srinagar Colony, Hydrabad','Manan pvt. ltd. ','sales@manan.com','Mr. Rushikesh Brahmbhatt','+91 9999999999'),
							(replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'3, Plot No 63, Nr Modi House, Sect 19c, Vashi, Navi Mumbai','Manan pvt. ltd. ','sales@manan.com','Mr. Rushikesh Brahmbhatt','+91 9999999910');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

TRUNCATE TABLE `product`;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (replace(uuid(),'-',''),NULL,now(),now(),NULL,now(),1,'Nilkamal','NI3002','2021-08-10',350.00,'Nilkamal Chair NI3002'),
							 (replace(uuid(),'-',''),NULL,now(),now(),NULL,now(),1,'Sukhi','SU3002','2021-08-10',600.50,'Sukhi Chair SU3002'),
							 (replace(uuid(),'-',''),NULL,now(),now(),NULL,now(),1,'Sonam','SO3002','2021-08-10',210.00,'Sonam Chair SO3002'),
							 (replace(uuid(),'-',''),NULL,now(),now(),NULL,now(),1,'Zopper','ZO3002','2021-08-10',510.00,'Zopper Chair ZO3002'),
							 (replace(uuid(),'-',''),NULL,now(),now(),NULL,now(),1,'MoorDOT','MO9221','2021-08-10',250.00,'MoorDOT Chair MO9221'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Vernex','VE3122','2021-08-10',910.00,'Vernex Chair VE3122'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Errty','ER1002','2021-08-10',1210.00,'Errty Chair ER1002'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Mothan','MO1002','2021-08-10',2210.00,'Mothan Chair MO1002'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Moveza','MO3102','2021-08-10',730.00,'Moveza Chair MO3102'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Wingway','WI3002','2021-08-10',640.00,'Wingway Chair WI3002'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Arpenz','AR3002','2021-08-10',820.00,'Arpenz Chair AR3002'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Zetron','ZE1005','2021-08-10',410.00,'Zetron Chair ZE1005'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Luzofa','LU1002','2021-08-10',912.00,'Luzofa Chair LU1002'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Nilkamal','NI3005','2021-08-10',730.00,'Nilkamal Chair NI3005'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Sukhi','SU3005','2021-08-10',820.00,'Sukhi Chair SU3005'),
							 (replace(uuid(),'-',''),NULL,now(),NULL,NULL,now(),0,'Zopper','ZO0002','2021-08-10',110.00,'Zopper Chair ZO0002');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
