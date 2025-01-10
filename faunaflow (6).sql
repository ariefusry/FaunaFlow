-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 10, 2025 at 12:28 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `faunaflow`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','user') NOT NULL,
  `employee_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `username`, `password`, `role`, `employee_id`) VALUES
(1, 'admin', 'admin123', 'admin', NULL),
(2, 'user', 'user123', 'user', NULL),
(9, 'aripgg', 'arip1234', 'admin', NULL),
(19, 'Mizan', 'zdr123', 'admin', NULL),
(20, 'Fadil', 'fadil123', 'user', 21);

-- --------------------------------------------------------

--
-- Table structure for table `data_processing_log`
--

CREATE TABLE `data_processing_log` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `change_description` text NOT NULL,
  `change_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_processing_log`
--

INSERT INTO `data_processing_log` (`id`, `username`, `change_description`, `change_time`) VALUES
(359, 'aripgg', 'Deleted all logs', '2025-01-09 19:54:41'),
(360, 'aripgg', 'User logged in', '2025-01-09 19:59:32'),
(361, 'aripgg', 'Created admin account for username: Mizan', '2025-01-09 19:59:57'),
(362, 'aripgg', 'Created account for username: Fadil', '2025-01-09 20:00:32'),
(363, 'aripgg', 'Added kandang with ukuran: 150, tipe: Kandang Semi-Akuatik dengan Area Berjemur, spesialitas: Area Berjemur, Kolam, Tempat Bersembunyi', '2025-01-09 20:01:14'),
(364, 'aripgg', 'Added kandang with ukuran: 1, tipe: a, spesialitas: a', '2025-01-09 20:01:33'),
(365, 'aripgg', 'Deleted kandang with ID: 57', '2025-01-09 20:01:40'),
(366, 'aripgg', 'Edited kandang with ID: 56, ukuran: 170, tipe: Kandang Semi-Akuatik dengan Area Berjemur, spesialitas: Area Berjemur, Kolam, Tempat Bersembunyi', '2025-01-09 20:01:55'),
(367, 'aripgg', 'Added hewan: Buaya', '2025-01-09 20:02:21'),
(368, 'aripgg', 'Added hewan: a', '2025-01-09 20:02:31'),
(369, 'aripgg', 'Deleted hewan with ID: 34', '2025-01-09 20:02:36'),
(370, 'aripgg', 'Edited hewan with ID: 33', '2025-01-09 20:02:49'),
(371, 'aripgg', 'Set hewan with ID: 33 to kandang with ID: 56', '2025-01-09 20:03:10'),
(372, 'aripgg', 'Removed employee with ID: 20 and associated account', '2025-01-09 20:03:46'),
(373, 'aripgg', 'Edited employee with ID: 21', '2025-01-09 20:04:07'),
(374, 'aripgg', 'Assigned random jobdesks', '2025-01-09 20:04:13'),
(375, 'aripgg', 'Deleted all JobdeskKaryawan data', '2025-01-09 20:04:21'),
(376, 'aripgg', 'Added stock: Pistol in category: Emergency', '2025-01-09 20:05:34'),
(377, 'aripgg', 'Added stock: Pistol in category: Emergency', '2025-01-09 20:05:34'),
(378, 'aripgg', 'Updated stock with ID: 27', '2025-01-09 20:05:52'),
(379, 'aripgg', 'Deleted stock with ID: 27', '2025-01-09 20:05:57'),
(380, 'aripgg', 'User logged out', '2025-01-09 20:06:12'),
(381, 'Fadil', 'User logged in', '2025-01-09 20:06:56'),
(382, 'Fadil', 'Submitted report by: Fadil', '2025-01-09 20:07:48'),
(383, 'Fadil', 'Updated stock with ID: 26', '2025-01-09 20:08:21'),
(384, 'Fadil', 'Submitted report by: Fadil', '2025-01-09 20:08:50'),
(385, 'Fadil', 'User logged out', '2025-01-09 20:08:52'),
(386, 'aripgg', 'User logged in', '2025-01-09 20:08:58'),
(387, 'aripgg', 'Deleted report with ID: 19', '2025-01-09 20:09:25'),
(388, 'aripgg', 'Deleted all reports', '2025-01-09 20:09:36'),
(389, 'aripgg', 'User logged out', '2025-01-09 20:10:03'),
(390, 'aripgg', 'User logged in', '2025-01-10 06:01:36'),
(391, 'aripgg', 'Added kandang with ukuran: 1, tipe: a, spesialitas: a', '2025-01-10 06:05:12'),
(392, 'aripgg', 'Deleted kandang with ID: 58', '2025-01-10 06:05:24'),
(393, 'aripgg', 'User logged in', '2025-01-10 06:14:48'),
(394, 'aripgg', 'Set hewan with ID: 29 to kandang with ID: 55', '2025-01-10 06:15:17'),
(395, 'aripgg', 'User logged in', '2025-01-10 06:16:20'),
(396, 'aripgg', 'User logged in', '2025-01-10 06:21:50'),
(397, 'aripgg', 'User logged in', '2025-01-10 06:29:29'),
(398, 'aripgg', 'User logged in', '2025-01-10 06:49:32'),
(399, 'aripgg', 'User logged in', '2025-01-10 11:20:02');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `usia` int(11) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `notel` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id`, `nama`, `usia`, `alamat`, `notel`) VALUES
(21, 'Fadil Muhammad', 23, 'Jl. Buah Batu', '0893213133');

-- --------------------------------------------------------

--
-- Table structure for table `gudang`
--

CREATE TABLE `gudang` (
  `idGudang` int(11) NOT NULL,
  `namaGudang` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gudang`
--

INSERT INTO `gudang` (`idGudang`, `namaGudang`) VALUES
(1, 'Gudang Utama'),
(2, 'Gudang Cadangan');

-- --------------------------------------------------------

--
-- Table structure for table `hewan`
--

CREATE TABLE `hewan` (
  `idHewan` int(11) NOT NULL,
  `idKandang` int(11) DEFAULT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `umur` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `berat` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hewan`
--

INSERT INTO `hewan` (`idHewan`, `idKandang`, `nama`, `umur`, `jumlah`, `berat`) VALUES
(23, NULL, 'Harimau', 7, 6, 130.00),
(24, NULL, 'Singa', 6, 10, 100.00),
(29, 55, 'Gajah', 5, 8, 800.00),
(33, 56, 'Buaya', 5, 10, 100.00);

-- --------------------------------------------------------

--
-- Table structure for table `jobdeskkaryawan`
--

CREATE TABLE `jobdeskkaryawan` (
  `id` int(11) NOT NULL,
  `nama_karyawan` varchar(100) NOT NULL,
  `senin` varchar(255) DEFAULT NULL,
  `selasa` varchar(255) DEFAULT NULL,
  `rabu` varchar(255) DEFAULT NULL,
  `kamis` varchar(255) DEFAULT NULL,
  `jumat` varchar(255) DEFAULT NULL,
  `sabtu` varchar(255) DEFAULT NULL,
  `minggu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kandang`
--

CREATE TABLE `kandang` (
  `id` int(11) NOT NULL,
  `ukuran` varchar(50) DEFAULT NULL,
  `tipe` varchar(50) DEFAULT NULL,
  `spesialitas` varchar(100) DEFAULT NULL,
  `idHewan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kandang`
--

INSERT INTO `kandang` (`id`, `ukuran`, `tipe`, `spesialitas`, `idHewan`) VALUES
(55, '1500', 'Kandang Terbuka dengan Area Berlumpur dan Kolam', 'Area Luas untuk Bergerak, Lumpur, Kolam Besar, Tempat Berteduh', NULL),
(56, '170', 'Kandang Semi-Akuatik dengan Area Berjemur', 'Area Berjemur, Kolam, Tempat Bersembunyi', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `laporan`
--

CREATE TABLE `laporan` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `laporan` text NOT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stok`
--

CREATE TABLE `stok` (
  `idStok` int(11) NOT NULL,
  `kategoriStok` varchar(50) NOT NULL,
  `namaStok` varchar(100) NOT NULL,
  `jumlahStok` int(11) NOT NULL,
  `Satuan` varchar(20) NOT NULL,
  `idGudang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stok`
--

INSERT INTO `stok` (`idStok`, `kategoriStok`, `namaStok`, `jumlahStok`, `Satuan`, `idGudang`) VALUES
(1, 'Makanan', 'Daging', 100, 'kg', 1),
(2, 'Makanan', 'Beras', 100, 'kg', 1),
(3, 'Kesehatan', 'Masker', 200, 'pcs', 1),
(4, 'Kesehatan', 'Obat', 50, 'box', 1),
(5, 'Maintenance', 'Obeng', 15, 'unit', 2),
(6, 'Maintenance', 'Tang', 10, 'unit', 2),
(7, 'Tour Guide', 'Peta', 50, 'lembar', 2),
(8, 'Tour Guide', 'Radio', 10, 'unit', 2),
(9, 'Emergency', 'APAR', 10, 'unit', 1),
(10, 'Emergency', 'P3K', 60, 'kotak', 1),
(13, 'Tour Guide', 'Alat Tulis', 700, 'paket', 2),
(26, 'Emergency', 'Pistol Kejut', 40, 'Unit', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `fk_employee_account` (`employee_id`);

--
-- Indexes for table `data_processing_log`
--
ALTER TABLE `data_processing_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `gudang`
--
ALTER TABLE `gudang`
  ADD PRIMARY KEY (`idGudang`);

--
-- Indexes for table `hewan`
--
ALTER TABLE `hewan`
  ADD PRIMARY KEY (`idHewan`),
  ADD KEY `fk_idKandang` (`idKandang`);

--
-- Indexes for table `jobdeskkaryawan`
--
ALTER TABLE `jobdeskkaryawan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kandang`
--
ALTER TABLE `kandang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idHewan` (`idHewan`);

--
-- Indexes for table `laporan`
--
ALTER TABLE `laporan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stok`
--
ALTER TABLE `stok`
  ADD PRIMARY KEY (`idStok`),
  ADD KEY `idGudang` (`idGudang`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `data_processing_log`
--
ALTER TABLE `data_processing_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=400;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `gudang`
--
ALTER TABLE `gudang`
  MODIFY `idGudang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `hewan`
--
ALTER TABLE `hewan`
  MODIFY `idHewan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `jobdeskkaryawan`
--
ALTER TABLE `jobdeskkaryawan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=198;

--
-- AUTO_INCREMENT for table `kandang`
--
ALTER TABLE `kandang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `laporan`
--
ALTER TABLE `laporan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `stok`
--
ALTER TABLE `stok`
  MODIFY `idStok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `fk_employee_account` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `data_processing_log`
--
ALTER TABLE `data_processing_log`
  ADD CONSTRAINT `data_processing_log_ibfk_1` FOREIGN KEY (`username`) REFERENCES `account` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hewan`
--
ALTER TABLE `hewan`
  ADD CONSTRAINT `fk_idKandang` FOREIGN KEY (`idKandang`) REFERENCES `kandang` (`id`);

--
-- Constraints for table `kandang`
--
ALTER TABLE `kandang`
  ADD CONSTRAINT `kandang_ibfk_1` FOREIGN KEY (`idHewan`) REFERENCES `hewan` (`idHewan`);

--
-- Constraints for table `stok`
--
ALTER TABLE `stok`
  ADD CONSTRAINT `stok_ibfk_1` FOREIGN KEY (`idGudang`) REFERENCES `gudang` (`idGudang`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
