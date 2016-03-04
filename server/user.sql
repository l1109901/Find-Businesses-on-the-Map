 CREATE TABLE `a6848435_db1`.`User` (
`id` INT( 11 ) NOT NULL AUTO_INCREMENT ,
`name` VARCHAR( 16 ) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL ,
`age` TINYINT( 4 ) NOT NULL ,
`username` VARCHAR( 16 ) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL ,
`password` VARCHAR( 16 ) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL ,
PRIMARY KEY ( `id` ) ,
UNIQUE (
`username`
)
) ENGINE = MYISAM