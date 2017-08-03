var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var path = require('path');
require('css-loader');

module.exports = {
    entry: {
        app: './app/app.js',
        //vendor: ['angular', 'filesaver.js', 'angular-route', 'angular-local-storage', 'jquery', 'bootstrap']
    },
    output: {
        path: __dirname + '/dist',
        filename: 'app.bundle.js'
    },
    // resolve: {
    //     alias: {
    //         constants: path.join(__dirname, 'constants', process.env.NODE_ENV)
    //     }
    // },
    devServer: {
        contentBase: path.join(__dirname, "app"),
        compress: true,
        port: 9000,
        headers: {
            'Access-Control-Allow-Origin': '*'
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
                loader: "style-loader!css-loader",
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
        }),
         new HtmlWebpackPlugin({
             template: './app/index.html'
         }),
        new CopyWebpackPlugin([{
            from: './app/images', to: 'images'
        }])
    ]
};
