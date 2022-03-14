INSERT INTO template (slug, content, content_type)
VALUES ('email-verification', '<!DOCTYPE html>
<html>
<head>
    <title>Email verification</title>
    <style>
        .content {
            margin: auto;
            width: 600px;
        }
    </style>
</head>
<body>
    <div class="content">
        <p>Your verification code is {{ code }}.</p>
    </div>
</body>
</html>', 'text/html'),
       ('mobile-verification', 'Your verification code is {{ code }}.', 'text/plain');
