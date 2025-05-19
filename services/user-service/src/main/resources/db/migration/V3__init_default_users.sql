INSERT INTO users (username, id, email, password_hash)
VALUES ('admin', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'admin@gmail.com',
        '$2a$10$0G8b.jcF8pRG3Gi66sv6feKkR3ZUSCXDDA2QYkOyxT7wNJqCuPZ2a'),
       ('moderator', '9a9b7f1d-7f1f-4f3d-bb14-38f8b734d99e', 'moderator@gmail.com',
        '$2a$10$8qfrZJIPzXPMieFqSEzXteoLZPNYRa3Y1oGEYB6jOBGpBgKJo/dwK'),
       ('user', '3d6f0a56-8b19-4d74-9a8b-582f7c6bb6c3', 'user@gmail.com',
        '$2a$10$X7TzqbcVmKe9J5ZtkOyK5eZbYe5U9HYT.KRHL/NKL7ZxgCJntNG.a');


INSERT INTO users_roles (user_id, role_id)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 4),
       ('9a9b7f1d-7f1f-4f3d-bb14-38f8b734d99e', 3),
       ('3d6f0a56-8b19-4d74-9a8b-582f7c6bb6c3', 1);

INSERT INTO user_profile (user_id, registration_date)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', now()),
       ('9a9b7f1d-7f1f-4f3d-bb14-38f8b734d99e', now()),
       ('3d6f0a56-8b19-4d74-9a8b-582f7c6bb6c3', now());