const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.send('Hello from Express!');
});

app.post('/login', (req, res) => {
  const { username, password } = req.body;
  // Fake login logic
  if (username === 'admin' && password === '1234') {
    res.json({ success: true });
  } else {
    res.json({ success: false });
  }
});

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
