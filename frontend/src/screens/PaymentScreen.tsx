import { useState } from 'react';
import { SafeAreaView, Text, View } from 'react-native';
import { api, Order, Payment } from '../api';
import { ActionButton } from '../components/ActionButton';
import { colors, money, styles } from '../theme';

export function PaymentScreen({ order, token, onComplete, onBack }: { order: Order; token: string; onComplete: (message: string) => void; onBack: () => void }) {
  const [payment, setPayment] = useState<Payment | null>(null), [busy, setBusy] = useState(false), [error, setError] = useState('');
  const simulate = async () => { setBusy(true); setError(''); try { const initialized = payment || await api.initializePayment(token, order.orderId); setPayment(initialized); await api.simulateWebhook(token, initialized.transactionReference); onComplete('Payment simulated successfully'); } catch (e) { setError(e instanceof Error ? e.message : 'Payment simulation failed'); } finally { setBusy(false); } };
  return <SafeAreaView style={styles.safe}><View style={styles.scroll}><Text style={{ color: colors.gold, fontWeight: '800', letterSpacing: 1 }}>TEST PAYMENT</Text><Text style={[styles.title, { marginTop: 8 }]}>Complete your order</Text><Text style={{ color: colors.muted, marginTop: 10 }}>Order {order.orderId}</Text><View style={{ backgroundColor: colors.white, padding: 18, marginTop: 25 }}><Text style={styles.muted}>Amount due</Text><Text style={{ fontSize: 30, fontWeight: '800', color: colors.forest, marginTop: 5 }}>{money(order.totalAmount)}</Text><Text style={{ color: colors.muted, lineHeight: 22, marginTop: 14 }}>This demo sends a `charge.success` webhook to the backend simulation endpoint. No real charge is made.</Text></View><ActionButton label={busy ? 'Processing...' : 'Simulate successful payment'} onPress={simulate} disabled={busy} />{error ? <Text style={styles.notice}>{error}</Text> : null}<ActionButton label="Back to orders" variant="outline" onPress={onBack} /></View></SafeAreaView>;
}
