import { StatusBar } from 'expo-status-bar';
import { useState } from 'react';
import { Pressable, SafeAreaView, Text, View } from 'react-native';
import { api } from '../api';
import { ActionButton } from '../components/ActionButton';
import { Field } from '../components/Field';
import { colors, styles } from '../theme';

export function AuthScreen({ onLogin, onRegistered, message }: { onLogin: (email: string, password: string) => Promise<void>; onRegistered: (email: string) => void; message: string }) {
  const [registering, setRegistering] = useState(false), [name, setName] = useState(''), [email, setEmail] = useState(''), [password, setPassword] = useState(''), [busy, setBusy] = useState(false), [error, setError] = useState('');
  const submit = async () => { setBusy(true); setError(''); try { if (registering) { await api.register({ nickName: name, email, password }); onRegistered(email); } else await onLogin(email, password); } catch (e) { setError(e instanceof Error ? e.message : 'Unable to continue'); } finally { setBusy(false); } };
  return <SafeAreaView style={[styles.safe, { padding: 26, justifyContent: 'center' }]}><StatusBar style="dark" /><Text style={{ color: colors.forest, fontSize: 18, fontWeight: '900', letterSpacing: 4, marginBottom: 48 }}>NOVA<Text style={{ color: '#b4794e' }}>•</Text></Text><Text style={{ fontSize: 32, fontWeight: '800', color: colors.ink }}>{registering ? 'Make room for good things' : 'Welcome back'}</Text><Text style={{ color: colors.muted, fontSize: 15, marginTop: 10, marginBottom: 22 }}>{registering ? 'Create an account to start shopping.' : 'Sign in to pick up where you left off.'}</Text>{registering && <Field placeholder="Nickname" value={name} onChangeText={setName} />}{<Field placeholder="Email address" autoCapitalize="none" keyboardType="email-address" value={email} onChangeText={setEmail} />}{<Field placeholder="Password" secureTextEntry value={password} onChangeText={setPassword} />}<ActionButton label={busy ? 'Please wait...' : registering ? 'Create account' : 'Sign in'} onPress={submit} disabled={busy} />{(error || message) ? <Text style={styles.notice}>{error || message}</Text> : null}<Pressable onPress={() => setRegistering(!registering)}><Text style={{ textAlign: 'center', color: '#34553a', fontWeight: '700', padding: 18 }}>{registering ? 'Already have an account? Sign in' : 'New here? Create an account'}</Text></Pressable></SafeAreaView>;
}
