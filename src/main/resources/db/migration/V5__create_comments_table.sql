CREATE TABLE IF NOT EXISTS `comments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `photo_id` varchar(12),
  `user_id` int(10) unsigned,
  `content` text,
  `created` datetime,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_comments_photo_id
    FOREIGN KEY (`photo_id`)
    REFERENCES `photos` (`id`),
  CONSTRAINT fk_comments_user_id
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;