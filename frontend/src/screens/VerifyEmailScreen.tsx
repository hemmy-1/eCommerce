import { StatusBar } from 'expo-status-bar';
import { useState } from 'react';
import { SafeAreaView, Text } from 'react-native';
import { api } from '../api';
import { ActionButton } from '../components/ActionButton';
import { Field } from '../components/Field';
import { colors, styles } from '../theme';

export function VerifyEmailScreen({ email, onVerified, message }: { email: string; onVerified: () => void; message: string }) {
  const [code, setCode] = useState(''), [busy, setBusy] = useState(false), [error, setError] = useState('');
  const verify = async () => { setBusy(true); setError(''); try { await api.verifyEmail({ email, code }); onVerified(); } catch (e) { setError(e instanceof Error ? e.message : 'Verification failed'); } finally { setBusy(false); } };
  return <SafeAreaView style={[styles.safe, { padding: 26, justifyContent: 'center' }]}><StatusBar style="dark" /><Text style={{ color: colors.forest, fontSize: 18, fontWeight: '900', letterSpacing: 4, marginBottom: 44 }}>NOVA<Text style={{ color: '#b4794e' }}>•</Text></Text><Text style={{ fontSize: 30, fontWeight: '800', color: colors.ink }}>Verify your email</Text><Text style={{ color: colors.muted, fontSize: 15, lineHeight: 23, marginTop: 10 }}>Enter the six-character code sent to {email}.</Text><Field placeholder="Verification code" autoCapitalize="characters" value={code} onChangeText={setCode} /><ActionButton label={busy ? 'Verifying...' : 'Verify email'} onPress={verify} disabled={busy || !code} />{(error || message) ? <Text style={styles.notice}>{error || message}</Text> : null}</SafeAreaView>;
}
