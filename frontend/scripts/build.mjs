import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, resolve } from 'node:path';

const sourcePath = resolve('src', 'index.js');
const outputPath = resolve('dist', 'bundle.js');

await mkdir(dirname(outputPath), { recursive: true });
const source = await readFile(sourcePath, 'utf8');
await writeFile(outputPath, source, 'utf8');

console.log(`Built ${outputPath}`);

