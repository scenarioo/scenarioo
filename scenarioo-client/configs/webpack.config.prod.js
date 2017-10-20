var webpack = require('webpack');
var merge = require('webpack-merge');
var commonConfig = require('./webpack.config.common');

var webpackConfig = merge(commonConfig, {
    devtool: 'source-map',

    output: {
        path: __dirname + '/../dist',
        filename: 'app.bundle.[hash].js'
    },

    plugins: [
        new webpack.NoEmitOnErrorsPlugin(),
        new webpack.optimize.UglifyJsPlugin()
    ]
});


module.exports = webpackConfig;
