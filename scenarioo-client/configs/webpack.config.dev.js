var path = require('path');
var merge = require('webpack-merge');
var commonConfig = require('./webpack.config.common');

var webpackConfig = merge(commonConfig, {

    devServer: {
        contentBase: path.join(__dirname, "app"),
        compress: true,
        port: 8500,
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        proxy: {
            "/rest": {
                "target": 'http://localhost:8080/scenarioo/rest',
                "pathRewrite": {'^/rest': ''}
            }
        }
    }
});

module.exports = webpackConfig;
