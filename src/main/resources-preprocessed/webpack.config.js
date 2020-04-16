const path = require('path');
const staticDirName = 'static';

module.exports = {
    entry: './src/main/resources-preprocessed/' + staticDirName + '/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, '../resources/' + staticDirName)
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
    }
};