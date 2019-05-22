var path = require('path');
var merge = require('webpack-merge');
var commonConfig = require('./webpack.config.common');

var webpackConfig = merge(commonConfig, {
    devtool: 'eval-source-map',

    devServer: {
        contentBase: path.join(__dirname, "app"),
        compress: true,
        port: 8500,
        publicPath: '/scenarioo/',
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        proxy: {
            "/scenarioo/rest": {
                "target": 'http://localhost:8080/scenarioo/rest',
                "pathRewrite": {'^/scenarioo/rest': ''}
            }
        }
    }
});

module.exports = webpackConfig;
