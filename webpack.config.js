const path = require('path');
const staticDir = 'src/main/resources/static';

module.exports = {
    entry: './' + staticDir + '/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, staticDir)
    },
    mode: 'development',
    watch: true
}