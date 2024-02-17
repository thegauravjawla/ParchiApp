#include<bits/stdc++.h>
using namespace std;

#define ff              first
#define ss              second
#define int             long long
#define pb              push_back
#define mp              make_pair
#define pii             pair<int,int>
#define vi              vector<int>
#define vvi             vector<vi>
#define mii             map<int,int>
#define pqb             priority_queue<int>
#define pqs             priority_queue<int,vi,greater<int> >
#define inf             1e18
#define all(x)          (x).begin(),(x).end()
#define deb(x)          cout << #x << "=" << x << '\n';
#define ps(x,y)         fixed<<setprecision(y)<<x
#define FIO             ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0)
const char ln='\n';
const char sp=' ';
const int mod=(int) 1e9+7;
int add(int a, int b) {a += b; if (a >= mod) return a - mod; return a;}
int sub(int a, int b) {a -= b; if (a < 0) return a + mod; return a;}
int mul(int a, int b) {return (a*b)%mod;}
int modpow(int x, int y, int p);
int modfact(int n, int p);
int modinv(int a, int m);
int lcm(int a, int b);
int gcd(int a, int b);
bool po2 (int x);
struct comp;

void print(string s) {
    int n = s.size();
    for (int i = 0; i < n-1; ++i) {
        if(s[i]==' '&&s[i+1]==' ') {

        } else {
            cout << s[i];
        }
    }
    if(s[n-1]!=' ')
        cout << s[n-1];
}

void solve() {
    int n;
    cin >> n;
    getchar();

    vector<string> s(n);
    vector<float> plus(n);
    for (int i = 0; i < n; ++i) {
        getline(cin, s[i]);
    }
    for (int i = 0; i < n; ++i) {
        cin >> plus[i];
    }

    for (int i = 0; i < n; ++i) {
        cout << "<item>";
        print(s[i]);
        cout << "</item>" << endl;
    }

    for (int i = 0; i < n; ++i) {
        cout << "plusValue.put(\"";
        print(s[i]);
        cout <<"\", " << plus[i] << "f);" << ln;
    }
}

int32_t main() {
//    FIO;
//    int c=1;
    int t = 1;
//    cin >> t;
    while(t--) {
//        cout << "Case #" << c++ << ": ";
        solve();
    }
    return 0;
}

int gcd(int a, int b) {
    if (a == 0) return b;
    return gcd(b % a, a);
}

int lcm(int a, int b) {
    return (a * b) / gcd(a, b);
}

bool po2(int x) {
    return x && (!(x&(x-1)));
}

int bin_pow(int x, int p) {
    if (p == 0) return 1;
    if (p & 1) return mul(x, bin_pow(x, p - 1));
    return bin_pow(mul(x, x), p / 2);
}
int rev(int x) {
    return bin_pow(x, mod - 2);
}

int modpow(int x, int y, int p) {
    int res = 1;
    x = x % p;
    if (x == 0)
        return 0;
    while (y > 0) {
        if (y & 1)
            res = (res * x) % p;
        y = y >> 1;
        x = (x * x) % p;
    }
    return res;
}

int modfact(int n, int p) {
    int ans = 1;
    for (int i = 1; i <= n; ++i) {
        ans = (ans*i)%p;
    }
    return ans;
}

int modinv(int a, int m) {
    int g = gcd(a, m);
    if (g != 1)
        return -1;
    else {
        return modpow(a, m - 2, m);
    }
}

struct comp {
    bool operator()(pii const& p1, pii const& p2) {
        if(p1.first>p2.first) return true;
        else if(p1.first<p2.first) return false;
        else return p1.second>p2.second;
    }
};