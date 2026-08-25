import { Pressable, ScrollView, Text, View } from 'react-native';
import { WishlistItem } from '../api';
import { money, styles } from '../theme';

export function WishlistScreen({ items, onRemove }: { items: WishlistItem[]; onRemove: (id: string) => void }) { return <ScrollView contentContainerStyle={styles.scroll}>{!items.length ? <Text style={styles.empty}>Save pieces here when you find the right one.</Text> : items.map(item => <View style={{ backgroundColor: '#fff', padding: 16, marginBottom: 8, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }} key={item.productId}><View><Text style={{ fontSize: 15, fontWeight: '700', color: '#292c28' }}>{item.productName}</Text><Text style={{ color: '#405742', fontWeight: '700', marginTop: 5 }}>{money(item.price)}</Text></View><Pressable onPress={() => onRemove(item.productId)}><Text style={styles.danger}>Remove</Text></Pressable></View>)}</ScrollView>; }
