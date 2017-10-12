var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var path = require('path');
require('css-loader');

/**
 * Env
 * Get npm lifecycle event to identify the environment
 */
var ENV = process.env.npm_lifecycle_event;
var isTest = ENV === 'test' || ENV === 'test-watch';
var isProd = ENV === 'build';

var webpackConfig = {

    entry: isTest ? void 0 : {
        app: './app/app.js'
    },

    output: isTest ? {} :  {
        path: __dirname + '/dist',
        filename: 'app.bundle.js'
    },
    devServer: {
        contentBase: path.join(__dirname, "app"),
        compress: true,
        port: 9000,
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        proxy: {
            "/rest": {
                "target": 'http://localhost:8080/scenarioo/rest',
                "pathRewrite": { '^/rest': '' },
                "changeOrigin": true,
                "secure": false
            }
        }
    },
    module: {
        loaders: [
            {
                test: /\.(jpg|jpeg|gif|png|ico)$/,
                include: /images/,
                loader:'file-loader?name=/images/[name].[ext]'
            },
            {
                test: /\.(json)$/,
                exclude: /node_modules/,
                loader:'file-loader?name=[name].[ext]&context=./manifest.json'
            },
            {
                test: /\.html$/,
                loader: "raw-loader",
                exclude: '/node_modules'
            },
            {
                test: /\.css$/,
                loader: isTest ? 'null-loader': "style-loader!css-loader",
                exclude: '/node_modules'
            },
            {
                test: /\.(woff|woff2|eot|ttf)$/i,
                loader: "file-loader?name=fonts/[name]-[hash].[ext]"
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url-loader?limit=10000&minetype=image/svg+xml"
            }
        ],
    },
    plugins: [
        new webpack.ProvidePlugin({
            jQuery: 'jquery',
            $: 'jquery',
            jquery: 'jquery',
            'window.jQuery': 'jquery'
        })
    ]
};

if (!isTest) {
    webpackConfig.plugins.push(new HtmlWebpackPlugin({
        template: './app/index.html'
    }));
    webpackConfig.plugins.push(new CopyWebpackPlugin([{
        from: './app/images', to: 'images'
    }]));
}

module.exports = webpackConfig;
