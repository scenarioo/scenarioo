var webpack = require('webpack');
var path = require('path');
require('css-loader');


var webpackConfig = {

    entry: void 0,

    output: {},

    resolve: {
        extensions: ['.js', '.ts']
    },

    devtool: 'inline-source-map',

    module: {
        rules: [
            {
                test: /\.ts$/,
                loader: 'ts-loader'
            },
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
        }),
        // Fixes Critical dependency: the request of a dependency is an expression
        // https://github.com/angular/angular/issues/20357
        new webpack.ContextReplacementPlugin(/\@angular(\\|\/)core(\\|\/)esm5/, path.join(__dirname, './app')),
    ]
};

module.exports = webpackConfig;
