CREATE TABLE IF NOT EXISTS `photos` (
  `id` varchar(12) NOT NULL,
  `user_id` int(10) unsigned,
  `filename` varchar(255),
  `created` datetime,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_photos_user_id
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;