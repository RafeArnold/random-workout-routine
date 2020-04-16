const path = require('path');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');

module.exports = {
    entry: './src/main/resources-preprocessed/static/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, '../resources/static/bundle')
    },
    mode: 'none',
    module: {
        rules: [
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.js$/,
                loader: 'babel-loader'
            }
        ]
    },
    plugins: [
        new CleanWebpackPlugin()
    ]
};