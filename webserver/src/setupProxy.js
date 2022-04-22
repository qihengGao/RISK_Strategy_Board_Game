const {createProxyMiddleware} = require('http-proxy-middleware');
module.exports = function (app) {

    const os = require('os');

    app.use(
        '/chat/*',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            ws: true,
            changeOrigin: true,
            onProxyReq: function(request) {
                request.setHeader("origin", "http://localhost:8080");
            },
            onProxyRes: function (proxyRes, req, res) {
                proxyRes.headers['Access-Control-Allow-Origin'] = "http://:"+os.hostname()+":8081"
            }
        })
    );
    app.listen(3000);
};