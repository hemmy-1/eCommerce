import { StyleSheet } from 'react-native';

export const colors = { ink: '#20221f', forest: '#28452f', sage: '#d9e4d2', cream: '#f7f7f3', white: '#fff', muted: '#7d8178', line: '#e4e4dc', gold: '#9a7045', danger: '#aa5749' };
export const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: colors.cream },
  screen: { flex: 1 },
  scroll: { padding: 22, paddingBottom: 40 },
  title: { fontSize: 25, fontWeight: '700', color: colors.ink },
  sectionTitle: { fontSize: 19, fontWeight: '700', color: colors.ink },
  muted: { color: colors.muted },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  input: { backgroundColor: colors.white, borderWidth: 1, borderColor: '#dedfd8', borderRadius: 8, padding: 14, marginTop: 12, fontSize: 15 },
  primary: { backgroundColor: colors.forest, padding: 15, borderRadius: 8, alignItems: 'center', marginTop: 14 },
  primaryText: { color: colors.white, fontWeight: '800', fontSize: 15 },
  outline: { borderWidth: 1, borderColor: '#9aaa99', padding: 13, borderRadius: 8, alignItems: 'center', marginTop: 10 },
  outlineText: { color: '#34553a', fontWeight: '700' },
  notice: { marginHorizontal: 22, marginBottom: 8, color: '#426341', backgroundColor: '#e5f0e0', padding: 10, borderRadius: 8 },
  empty: { color: colors.muted, textAlign: 'center', marginTop: 70, fontSize: 16, lineHeight: 24 },
  danger: { color: colors.danger, textAlign: 'center', padding: 14, fontWeight: '700' },
});
export const money = (value = 0) => `₦${Number(value).toLocaleString('en-NG', { minimumFractionDigits: 2 })}`;
