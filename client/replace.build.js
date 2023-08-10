const replace = require('replace-in-file');
const buildDate = process.argv[2];
const buildEnv = process.argv[3];

const optionsVersion = {
  files: 'src/environments/environment.ts',
  from: /BUILD-VERSION/g,
  to: buildEnv,
  allowEmptyPaths: false,
};

const optionsEnv = {
  files: 'src/environments/environment.ts',
  from: /BUILD-DATE/g,
  to: buildDate,
  allowEmptyPaths: false,
};

try {
  let changedFiles = replace.sync(optionsVersion);
  changedFiles = replace.sync(optionsEnv);
  console.log('Build date set: ' + buildDate);
  console.log('Build env set: ' + buildEnv);
} catch (error) {
  console.error('Error occurred:', error);
}
