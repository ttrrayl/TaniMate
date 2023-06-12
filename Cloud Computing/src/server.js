const express = require("express");
const mysql = require("mysql");
const jwt = require("jsonwebtoken");
require("dotenv").config();

const app = express();
app.use(express.json());

// Buat koneksi database
const db = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
});

// Terhubung ke database
db.connect((err) => {
  if (err) {
    throw err;
  }
  console.log("Terhubung ke database MySQL");
});

// Middleware autentikasi
function authenticateToken(req, res, next) {
  // Mendapatkan token dari header Authorization
  const token = req.headers.authorization && req.headers.authorization.split(' ')[1];

  if (token == null) {
    return res.status(401).json({ message: "Token tidak ditemukan" });
  }

  // Verifikasi token
  jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
    if (err) {
      return res.status(403).json({ message: "Token tidak valid" });
    }

    // Menyimpan data pengguna yang diverifikasi ke objek req
    req.user = user;
    next();
  });
}

// API register
app.post("/api/register", (req, res) => {
  const { username, email, password, confirmPassword } = req.body;

  if (password !== confirmPassword) {
    return res.status(400).json({ message: "Password dan konfirmasi password tidak sesuai" });
  }

  // Cek apakah username sudah terdaftar
  db.query("SELECT * FROM users WHERE username = ?", [username], (err, results) => {
    if (err) {
      throw err;
    }

    if (results.length > 0) {
      return res.status(409).json({ message: "Username sudah terdaftar" });
    }

    const newUser = { username, email, password };

    // Simpan pengguna baru ke database
    db.query("INSERT INTO users SET ?", newUser, (err, result) => {
      if (err) {
        throw err;
      }

      res.status(201).json({ message: "Pengguna berhasil didaftarkan" });
    });
  });
});

app.get("/api/register", (req, res) => {
  // Ambil semua pengguna dari database
  db.query("SELECT * FROM users", (err, results) => {
    if (err) {
      throw err;
    }

    res.json(results);
  });
});

app.put("/api/register/:id", (req, res) => {
  const { id } = req.params;
  const { username, email, password } = req.body;

  // Perbarui pengguna dengan ID tertentu di database
  db.query(
    "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?",
    [username, email, password, id],
    (err, result) => {
      if (err) {
        throw err;
      }

      res.json({ message: "Pengguna berhasil diperbarui" });
    }
  );
});

app.delete("/api/register/:id", (req, res) => {
  const { id } = req.params;

  // Hapus pengguna dengan ID tertentu dari database
  db.query("DELETE FROM users WHERE id = ?", [id], (err, result) => {
    if (err) {
      throw err;
    }

    res.json({ message: "Pengguna berhasil dihapus" });
  });
});

// API login
app.post("/api/login", (req, res) => {
  const { username, password } = req.body;

  // Cari pengguna berdasarkan username
  db.query(
    "SELECT * FROM users WHERE username = ?",
    [username],
    (err, results) => {
      if (err) {
        throw err;
      }

      if (results.length === 0) {
        // Jika pengguna tidak ditemukan
        res.status(401).json({ message: "Username atau password salah" });
      } else {
        const user = results[0];

        if (password === user.password) {
          // Jika password cocok
          const accessToken = generateAccessToken({ id: user.id });
          const refreshToken = jwt.sign({ id: user.id }, process.env.REFRESH_TOKEN_SECRET);
          saveRefreshToken(refreshToken, user.id);

          res.json({ message: "Login berhasil", accessToken, refreshToken });
        } else {
          // Jika password tidak cocok
          res.status(401).json({ message: "Username atau password salah" });
        }
      }
    }
  );
});

// API refresh token
app.post("/api/refresh-token", (req, res) => {
  const { refreshToken } = req.body;

  // Periksa keberadaan refresh token
  if (!refreshToken) {
    return res.status(401).json({ message: "Refresh token tidak ditemukan" });
  }

  // Verifikasi refresh token
  jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, user) => {
    if (err) {
      return res.status(403).json({ message: "Refresh token tidak valid" });
    }

    // Cek keberadaan refresh token di database
    db.query(
      "SELECT * FROM refresh_tokens WHERE token = ?",
      [refreshToken],
      (err, results) => {
        if (err) {
          throw err;
        }

        if (results.length === 0) {
          return res.status(403).json({ message: "Refresh token tidak valid" });
        }

        // Generate a new access token
        const accessToken = generateAccessToken({ id: user.id });

        res.json({ accessToken });
      }
    );
  });
});

// Fungsi untuk menghasilkan token akses
function generateAccessToken(user) {
  return jwt.sign(user, process.env.JWT_SECRET, { expiresIn: "15m" });
}

// Fungsi untuk menyimpan refresh token ke database
function saveRefreshToken(refreshToken, userId) {
  db.query(
    "INSERT INTO refresh_tokens (token, user_id) VALUES (?, ?)",
    [refreshToken, userId],
    (err) => {
      if (err) {
        throw err;
      }
    }
  );
}

// API terproteksi
app.get("/api/protected", authenticateToken, (req, res) => {
  res.json({ message: "Rute terproteksi berhasil diakses" });
});

// Jalankan server
const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`Server berjalan pada port ${port}`);
});