CREATE TABLE IF NOT EXISTS `likes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `photo_id` varchar(12),
  `user_id` int(10) unsigned,
  `created` datetime,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_likes_photo_id
    FOREIGN KEY (`photo_id`)
    REFERENCES `photos` (`id`),
  CONSTRAINT fk_likes_user_id
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;