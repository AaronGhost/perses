int main() {
  (void)'�';
  (void)u'�';
  (void)U'�';
  (void)L'�';
  static_assert((unsigned char)'�' == 0xE9, "");
  static_assert('��' == 0xE9E9, "");
}
