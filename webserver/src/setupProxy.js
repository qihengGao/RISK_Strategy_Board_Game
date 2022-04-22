const { createProxyMiddleware } = require('http-proxy-middleware');
module.exports = function(app) {
    app.use(
        '/chat/*',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            ws:true
        })
    );
    app.listen(3000);
};