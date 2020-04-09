const path = require('path');
const staticDirName = 'static';

module.exports = {
    entry: './src/main/resources/' + staticDirName + '/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, staticDirName)
    },
    mode: 'none'
}