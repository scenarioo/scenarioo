var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var path = require('path');
require('css-loader');


var webpackConfig = {

    entry: void 0,

    output: {},

    module: {
        loaders: [
            {
                test: /\.(jpg|jpeg|gif|png|ico)$/,
                include: /images/,
                loader: 'file-loader?name=/images/[name].[ext]'
            },
            {
                test: /\.html$/,
                loader: "raw-loader",
                exclude: '/node_modules'
            },
            {
                test: /\.css$/,
                loader: 'null-loader',
                exclude: '/node_modules'
            },
            {
                test: /\.less/,
                loader: "null-loader",
                exclude: '/node_modules'
            },
            {
                test: /\.(woff|woff2|eot|ttf)$/i,
                loader: 'null-loader'
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                loader: 'null-loader'
            }
        ],
    },
    plugins: [
        new webpack.ProvidePlugin({
            jQuery: 'jquery',
            $: 'jquery',
            jquery: 'jquery',
            'window.jQuery': 'jquery',
        })
    ]
};

module.exports = webpackConfig;
