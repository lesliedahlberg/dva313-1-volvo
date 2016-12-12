-- 
-- Table structure for table `Alias`
-- 

CREATE TABLE `Alias` (
  `Name` varchar(255) NOT NULL DEFAULT 'N/A',
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;




CREATE TABLE `Machine` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;


-- --------------------------------------------------------

-- 
-- Table structure for table `Session`
-- 

CREATE TABLE `Session` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Score` int(11) NOT NULL DEFAULT '0',
  `TimePlayed` varchar(255) NOT NULL,
  `Duration` varchar(255) NOT NULL DEFAULT '0',
  `Published` tinyint(1) NOT NULL DEFAULT '1',
  `IdAlias` int(11) NOT NULL,
  `IdMachine` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_alias` (`IdAlias`),
  KEY `fk_machine` (`IdMachine`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 AUTO_INCREMENT=32 ;


-- --------------------------------------------------------

-- 
-- Table structure for table `ValueTime`
-- 

CREATE TABLE `ValueTime` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Value` int(11) NOT NULL,
  `Time` varchar(255) NOT NULL,
  `Type` varchar(255) NOT NULL,
  `IdSession` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_session` (`IdSession`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=latin1 AUTO_INCREMENT=129 ;


-- 
-- Constraints for dumped tables
-- 

-- 
-- Constraints for table `Session`
-- 
ALTER TABLE `Session`
  ADD CONSTRAINT `fk_alias` FOREIGN KEY (`IdAlias`) REFERENCES `Alias` (`Id`),
  ADD CONSTRAINT `fk_machine` FOREIGN KEY (`IdMachine`) REFERENCES `Machine` (`Id`);

-- 
-- Constraints for table `ValueTime`
-- 
ALTER TABLE `ValueTime`
  ADD CONSTRAINT `fk_session` FOREIGN KEY (`IdSession`) REFERENCES `Session` (`Id`);
