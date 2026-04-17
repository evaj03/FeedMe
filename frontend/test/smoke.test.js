import test from 'node:test';
import assert from 'node:assert/strict';

test('frontend package metadata is valid', async () => {
  const packageModule = await import('../package.json', { with: { type: 'json' } });

  assert.equal(packageModule.default.name, 'feedme-frontend');
  assert.equal(packageModule.default.private, true);
});

