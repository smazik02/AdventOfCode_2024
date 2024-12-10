import fs from 'node:fs';

const data = fs
	.readFileSync('input.txt', 'utf8')
	.split('\n')
	.map((line) => line.split(' ').map((num) => parseInt(num, 10)));

let safeRows = 0;
for (const row of data) {
	let asc: boolean | null = null;
	let safe = true;

	const pairCheck = (num: number, idx: number, other: number[]) => {
		const pairDir = other[idx + 1] - num;

		if (pairDir === 0) safe = false;

		if (asc == null) asc = pairDir > 0;

		if (asc !== pairDir > 0) safe = false;

		if (Math.abs(pairDir) > 3) safe = false;
	};

	row.slice(0, -1).forEach((num, idx) => pairCheck(num, idx, row));

	if (!safe) {
		for (let i = 0; i < row.length; i++) {
			asc = null;
			safe = true;
			const checked = row.filter((_, idx) => idx !== i);
			checked
				.slice(0, -1)
				.forEach((num, idx) => pairCheck(num, idx, checked));
			if (safe) break;
		}
	}

	console.log(`Row ${row}: ${safe ? 'SAFE' : 'UNSAFE'}`);
	safeRows += safe ? 1 : 0;
}

console.log(`Safe rows: ${safeRows}`);
