const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    mode: 'production',
    module: {
        rules: [
            {
                test: /\.(eot|otf|svg|ttf|woff)$/i,
                loader: 'file-loader',
                options: {
                    publicPath: '/rwr/'
                }
            }
        ]
    }
});