-- Bảng `users`: Lưu thông tin người dùng (admin, user thường, nghệ sĩ)
CREATE TABLE users (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính, tự tăng
username VARCHAR(100) NOT NULL,                -- Tên đăng nhập
full_name VARCHAR(100) NOT NULL,               -- Họ và tên người dùng
avatar VARCHAR(100) NOT NULL,                  -- Link ảnh đại diện
password VARCHAR(60) NOT NULL,                 -- Mật khẩu đã mã hóa
phone VARCHAR(11),                             -- Số điện thoại
email VARCHAR(100),                            -- Địa chỉ email
role VARCHAR(20),                              -- Vai trò: admin / user / artist
dob DATE,                                      -- Ngày sinh
is_deleted BOOLEAN,                            -- Đánh dấu xóa mềm (true = đã xóa)
created_at DATETIME,                           -- Ngày tạo người dùng
modified_at DATETIME                           -- Ngày cập nhật cuối
);

-- Bảng `artists`: Lưu thông tin chi tiết của nghệ sĩ, liên kết với 1 user cụ thể
CREATE TABLE artists (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
artist_name VARCHAR(100) NOT NULL,             -- Tên nghệ danh
image VARCHAR(150),                            -- Ảnh đại diện nghệ sĩ
bio TEXT,                                      -- Tiểu sử nghệ sĩ
user_id INT UNIQUE,                            -- Khóa ngoại: liên kết user làm nghệ sĩ
is_deleted BOOLEAN,                            -- Đánh dấu xóa mềm
created_at DATETIME,                           -- Ngày tạo
modified_at DATETIME,                          -- Ngày cập nhật cuối
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Xóa user thì xóa luôn artist
);

-- Bảng `categories`: Lưu các thể loại nhạc (pop, rock, ballad, v.v.)
CREATE TABLE categories (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
name VARCHAR(50),                              -- Tên thể loại
is_deleted BOOLEAN,                            -- Xóa mềm
created_at DATETIME,                           -- Ngày tạo
modified_at DATETIME                           -- Ngày cập nhật
);

-- Bảng `songs`: Lưu bài hát trong hệ thống
CREATE TABLE songs (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
name VARCHAR(100),                             -- Tên bài hát
file_path VARCHAR(200),                        -- Đường dẫn file nhạc
image VARCHAR(200),                            -- Ảnh đại diện bài hát
category_id INT,                               -- FK: thể loại bài hát
artist_id INT,                                 -- FK: nghệ sĩ trình bày
is_deleted BOOLEAN,                            -- Xóa mềm
created_at DATETIME,                           -- Ngày tạo
modified_at DATETIME,                          -- Ngày cập nhật
FOREIGN KEY (category_id) REFERENCES categories(id), -- Thể loại
FOREIGN KEY (artist_id) REFERENCES artists(id)       -- Nghệ sĩ
);

-- Bảng `playlists`: Lưu thông tin playlist do user tạo
CREATE TABLE playlists (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
name VARCHAR(100),                             -- Tên playlist
image VARCHAR(200),                            -- Ảnh đại diện playlist
user_id INT,                                   -- FK: người tạo playlist
is_deleted BOOLEAN,                            -- Xóa mềm
created_at DATETIME,                           -- Ngày tạo
modified_at DATETIME,                          -- Ngày cập nhật
FOREIGN KEY (user_id) REFERENCES users(id)     -- Người sở hữu playlist
);

-- Bảng `playlist_songs`: Bản trung gian liên kết nhiều bài hát vào playlist
CREATE TABLE playlist_songs (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
playlist_id INT,                               -- FK: playlist
song_id INT,                                   -- FK: bài hát
created_at DATETIME,                           -- Ngày thêm bài hát vào playlist
FOREIGN KEY (playlist_id) REFERENCES playlists(id),  -- Liên kết playlist
FOREIGN KEY (song_id) REFERENCES songs(id)            -- Liên kết bài hát
);

-- Bảng `comments`: Lưu bình luận của user về bài hát
CREATE TABLE comments (
id INT(11) AUTO_INCREMENT PRIMARY KEY,         -- Khóa chính
user_id INT,                                   -- FK: người bình luận
song_id INT,                                   -- FK: bài hát được bình luận
content TEXT,                                  -- Nội dung bình luận
created_at DATETIME,                           -- Thời gian bình luận
FOREIGN KEY (user_id) REFERENCES users(id),    -- Người bình luận
FOREIGN KEY (song_id) REFERENCES songs(id)     -- Bài hát
);

