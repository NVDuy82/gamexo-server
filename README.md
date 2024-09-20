# Game XO

## Giới thiệu
**Game XO Server** là server cho trò chơi XO cổ điển (Tic-Tac-Toe) nhưng được mở rộng luật chơi lớn ăn nhỏ được phát triển bằng Java. Trò chơi hỗ trợ nhiều người chơi qua mạng với server xử lý giao tiếp thông qua Java Socket.

## Tính năng
- Server xử lý kết nối nhiều người chơi với Java Socket và đa luồng (multithreading).
- Quản lý trạng thái trò chơi và dữ liệu giữa các người chơi.
- Thống kê điểm số người chơi khi chơi online.

## Công nghệ sử dụng
- **Java**: Ngôn ngữ lập trình chính.
- **Java Socket**: Xử lý giao tiếp mạng giữa client và server.

## Yêu cầu hệ thống
- JDK 21 trở lên.
- MySQL.

## Hướng dẫn 

Có thể sử dụng file từ đường dẫn `out/artifacts/xogame_jar/xogame.jar` nhưng cần cài đặt MySQL và cấu hình giống file code như sau:
- URL: jdbc:mysql://localhost:3306/game_xo
- Username: root
- Password: 123456

## Clone repo
```bash
git clone https://github.com/NVDuy82/gamexo-server.git
```
```
cd gamexo-server
```
